export interface KillOutput {
  id: number,
  timeOfDeath: string,
  story: string | null,
  lat: number | null,
  lng: number | null,
  killerID: number,
  biteCode: string
}
