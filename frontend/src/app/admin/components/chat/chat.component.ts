import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Message} from "../../../models/input/message.model";
import {PlayerInfoFull} from "../../../models/input/player-info-full.model";

@Component({
  selector: 'app-chat-admin',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent {

  @Input()
  public chatMessages: Message[] | null = null;

  public selectedChat: string = "GLOBAL";

  public submitText: String = "";

  private selectedSquadID = 0;
  private selectedPlayerID = 0;

  @Input()
  public squads = null;

  @Input()
  public players: PlayerInfoFull[] = [];

  public isMobile: boolean;

  constructor() {
    this.isMobile = window.innerWidth < 768;
  }

  // Emits a request to load the correct chat
  @Output() globalChat: EventEmitter<any> = new EventEmitter<any>();
  displayGlobal() {
    this.selectedChat = "GLOBAL";
    this.globalChat.emit();
  }

  @Output() humanChat: EventEmitter<any> = new EventEmitter<any>();
  displayHuman() {
    this.selectedChat = "HUMAN";
    this.humanChat.emit();
  }
  @Output() zombieChat: EventEmitter<any> = new EventEmitter<any>();
  displayZombie() {
    this.selectedChat = "ZOMBIE";
    this.zombieChat.emit();
  }

  // Emits the message to be sent and clears the input
  @Output() sendChat: EventEmitter<any> = new EventEmitter<any>();
  submitChat() {
    if (this.submitText != "") {
      this.sendChat.emit(this.submitText);
      this.submitText = "";
    }
  }

  @Output() squadLoad: EventEmitter<any> = new EventEmitter<any>();
  loadSquadChat(event: Event) {
    this.selectedChat = "SQUAD"
    this.selectedSquadID = parseInt((event.target as HTMLSelectElement).value);
    this.squadLoad.emit(this.selectedSquadID);
  }

  @Output() playerLoad: EventEmitter<any> = new EventEmitter<any>();
  loadPlayerChat(event: Event) {
    this.selectedChat = "PLAYER"
    this.selectedPlayerID = parseInt((event.target as HTMLSelectElement).value);
    this.playerLoad.emit(this.selectedPlayerID);
  }
}
