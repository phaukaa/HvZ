export interface ActiveGame {
  id: number,
  name: string,
  gameState: string,
  playerAmount: number,
  startTime: string | null,
  endTime: string | null,
  playerID: number | null
}
