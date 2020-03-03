import {MessageType} from "./message-type.model";

export interface IChatMessage {
  type: MessageType;
  sender: string;
  content?: string;
}

export class ChatMessage implements IChatMessage {

  constructor(public type: MessageType, public sender: string, public content?: string) {
  }
}

