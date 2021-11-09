import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Message} from "../../../models/input/message.model";

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent {

  @Input()
  public chatMessages: Message[] | null = null;
  @Input()
  public playerIsHuman: boolean = true;
  @Input()
  public playerHasSquad: boolean = false;

  @Input()
  public playerHasGame: boolean = false;

  public selectedChat: string = "GLOBAL";

  public submitText: String = "";

  public isMobile: boolean;

  constructor() {
    this.isMobile = window.innerWidth < 768; }

  // Emits a request to load the correct chat
  @Output() globalChat: EventEmitter<any> = new EventEmitter<any>();
  displayGlobal() {
    this.selectedChat = "GLOBAL";
    this.globalChat.emit();
  }

  @Output() factionChat: EventEmitter<any> = new EventEmitter<any>();
  displayFaction() {
    if (this.playerIsHuman) {
      this.selectedChat = "HUMAN";
    }
    else {
      this.selectedChat = "ZOMBIE";
    }
    this.factionChat.emit();
  }

  @Output() squadChat: EventEmitter<any> = new EventEmitter<any>();
  displaySquad() {
    this.selectedChat = "SQUAD";
    this.squadChat.emit();
  }

  // Emits the message to be sent and clears the input
  @Output() sendChat: EventEmitter<any> = new EventEmitter<any>();
  submitChat() {
    if (this.submitText != "") {
      this.sendChat.emit(this.submitText);
      this.submitText = "";
    }
  }
}
