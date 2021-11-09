export interface MapMarker {
  id: number,
  type: string,
  description: string | null,
  position: {
    lat: number,
    lng: number
  },
  options: {
    icon: string,
    opacity: number
  },
  title: string
}
