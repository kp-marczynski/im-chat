import {Component, OnInit} from '@angular/core';
import {ChatService} from "./chat.service";
import {IChatMessage} from "../../model/chat-message.model";

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss']
})
export class ChatComponent implements OnInit {

  messages: IChatMessage[] = [];
  isUserPresent = false;

  constructor(private chatService: ChatService) {
  }

  ngOnInit() {
    this.checkUserPresent();
    this.chatService.subscribePublicMessages().subscribe(message => this.messages.push(message));
  }

  joinAsUser = (username: string) => this.chatService.joinPublicChat(username, this.checkUserPresent);

  checkUserPresent = (): void => {
    this.isUserPresent = this.chatService.isUserPresent();
  };

  sendMessage = (newMessage: string): void => this.chatService.sendPublicMessage(newMessage);
}
