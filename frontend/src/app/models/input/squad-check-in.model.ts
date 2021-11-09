export interface SquadCheckIn {
  id: number,
  time: string,
  lat: number,
  lng: number,
  member: {
    name: string,
    human: boolean
  }
}
