export interface Kill {
  id: number,
  timeOfDeath: string,
  story: string | null,
  lat: number | null,
  lng: number | null,
  killerName: string,
  victimName: string
}
