import {MessageType} from "./message-type.model";

export interface IChatMessage {
  type: MessageType;
  timestamp: Date;
  sender: string;
  content?: string;
}

export class ChatMessage implements IChatMessage {
  public timestamp: Date;

  constructor(public type: MessageType, public sender: string, public content?: string) {
    this.timestamp = new Date();
  }
}

