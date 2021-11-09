import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { GameInfoPage} from "../pages/game-info.page";
import {AdminPage} from "../../admin/pages/admin.page";
import {baseURL} from "../../../assets/base-url";

export class WebSocketAPI {
  webSocketEndPoint: string = baseURL + 'api/websocket';
  topic: string = "/socket/notify";
  stompClient: any;
  game: GameInfoPage | AdminPage;
  constructor(appComponent: GameInfoPage | AdminPage){
    this.game = appComponent;
  }

  /**
   * Method for connecting and subscribing to the backend
   */
  _connect() {
    console.log("Initialize WebSocket Connection");
    let ws = new SockJS(this.webSocketEndPoint);
    this.stompClient = Stomp.over(ws);
    const _this = this;
    _this.stompClient.connect({}, function () {
      _this.stompClient.subscribe(_this.topic, function (sdkEvent: any) {
        _this.onMessageReceived(sdkEvent);
      });
      //_this.stompClient.reconnect_delay = 2000;
    }, this.errorCallBack);
  };

  /**
   * Method for disconnecting from the backend
   */
  _disconnect() {
    if (this.stompClient !== null) {
      this.stompClient.disconnect();
    }
    console.log("Disconnected");
  }

  // on error, schedule a reconnection attempt
  errorCallBack(error: string) {
    console.log("errorCallBack -> " + error)
    setTimeout(() => {
      this._connect();
    }, 5000);
  }

  /**
   * Send message to sever via web socket (if needed, not used)
   * @param {*} message
   */
  _send(message: string | number) {
    console.log("ID " + message + "calling api via web socket");
    this.stompClient.send("/app/websocket", {}, JSON.stringify(message));
  }

  /**
   * Method for handling a message recieved from the backend
   * Only used for notification for now
   * @param message
   */
  onMessageReceived(message: String) {
    console.log("New message recieved!");
    this.game.handleMessage();
  }
}
