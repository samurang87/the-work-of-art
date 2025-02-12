export enum Medium {
  ACRYLIC = "acrylic",
  CHARCOAL = "charcoal",
  COLOR_PENCILS = "color pencils",
  CRAYONS = "crayons",
  DIGITAL = "digital",
  GOUACHE = "gouache",
  INK = "ink",
  MARKERS = "markers",
  OIL = "oil",
  PAN_PASTELS = "pan pastels",
  PASTELS = "pastels",
  PENCILS = "pencils",
  WATERCOLORS = "watercolors",
}

export type User = {
  id: string;
  name: string;
  bio: string | null;
  imageUrl: string | null;
  mediums: Medium[];
};

export type UserEditRequestPayload = {
  bio: string | null;
  imageUrl: string | null;
  mediums: Medium[];
};

export type Material = {
  name: string;
  identifier: string | null;
  brand: string | null;
  line: string | null;
  type: string | null;
  medium: Medium | null;
};

export type WorkOfArt = {
  id: string;
  user: string;
  userName: string;
  challengeId: string | null;
  title: string;
  description: string | null;
  imageUrl: string;
  medium: Medium;
  materials: Material[] | null;
  createdAt: string;
};

export type WorkOfArtCreateRequest = {
  user: string;
  userName: string;
  challengeId: string | null;
  title: string;
  description: string | null;
  imageUrl: string;
  medium: string;
  materials: MaterialCreateRequest[];
};

export type MaterialCreateRequest = {
  name: string;
  identifier: string | null;
  brand: string | null;
  line: string | null;
  type: string | null;
  medium: string | null;
};

export type SignatureResponse = {
  signature: string;
  timestamp: string;
  apiKey: string;
  cloudName: string;
  uploadPreset: string;
};
