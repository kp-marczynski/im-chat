import {Injectable, OnDestroy} from '@angular/core';
import {Client, Message, over, StompSubscription} from "@stomp/stompjs";
import {BehaviorSubject, Observable, Subscription} from "rxjs";
import * as SockJS from 'sockjs-client';
import {SocketClientState} from "../model/socket-client-state.model";
import {filter, first, switchMap} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class SocketClientService implements OnDestroy {
  private readonly client: Client;
  private state: BehaviorSubject<SocketClientState>;

  constructor() {
    this.client = over(new SockJS('/ws'));
    this.state = new BehaviorSubject<SocketClientState>(SocketClientState.ATTEMPTING);
    this.client.connect({}, () => this.state.next(SocketClientState.CONNECTED));
  }

  ngOnDestroy() {
    this.connect().pipe(first()).subscribe(client => client.disconnect(null));
  }

  private connect = (): Observable<Client> =>
    new Observable<Client>(observer =>
      this.state.pipe(filter(state => state === SocketClientState.CONNECTED))
        .subscribe(() => observer.next(this.client))
    );


  onMessage = (topic: string, handler = SocketClientService.jsonHandler): Observable<any> =>
    this.connect().pipe(first(), switchMap(inst => {
      return new Observable<any>(observer => {
        const subscription: StompSubscription = inst.subscribe(topic, message => observer.next(handler(message)));
        return () => inst.unsubscribe(subscription.id);
      });
    }));

  send = (topic: string, payload: any): Subscription =>
    this.connect()
      .pipe(first())
      .subscribe(inst => inst.send(topic, {}, JSON.stringify(payload)));

  static jsonHandler = (message: Message): any => JSON.parse(message.body);

  onPlainMessage = (topic: string): Observable<string> => this.onMessage(topic, SocketClientService.textHandler);

  static textHandler = (message: Message): string => message.body;
}
