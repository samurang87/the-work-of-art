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
    WATERCOLORS = "watercolor"
}

export type User = {
    id: string;
    name: string;
    bio: string | null;
    imageUrl: string | null;
    mediums: Medium[];
}
