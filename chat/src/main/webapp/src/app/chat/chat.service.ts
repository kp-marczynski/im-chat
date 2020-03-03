import {Injectable} from '@angular/core';
import {SocketClientService} from "../../services/socket-client.service";
import {ChatMessage, IChatMessage} from "../../model/chat-message.model";
import {MessageType} from "../../model/message-type.model";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private currentUsername: string;

  constructor(private socketClientService: SocketClientService) {
    this.subscribePublicMessages();
  }

  public subscribePublicMessages = (): Observable<IChatMessage> =>
    this.socketClientService.onMessage('/topic/public');


  public joinPublicChat = (username: string, callback: Function): void => {
    this.currentUsername = username;
    this.socketClientService.send("/app/chat.addUser",
      new ChatMessage(MessageType.JOIN, username)
    );
    callback();
  };

  public isUserPresent = () => !!this.currentUsername;

  public sendPublicMessage = (message: string): void => {
    if (this.currentUsername) {
      this.socketClientService.send("/app/chat.sendMessage",
        new ChatMessage(MessageType.CHAT, this.currentUsername, message)
      );
    } else {
      alert('current user not specified')
    }
  }
}
