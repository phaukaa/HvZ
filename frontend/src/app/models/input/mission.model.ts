export interface Mission {
  id: number,
  name: string,
  description: string,
  lat: number,
  lng: number,
  startTime: string | null,
  endTime: string | null,
  human: boolean,
  gameId: number
}
