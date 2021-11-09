export interface Message {
  id: number,
  global: boolean,
  human: boolean,
  sender: string,
  time: string,
  content: string
}
