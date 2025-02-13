import { useEffect, useRef } from "react";
import { SignatureResponse } from "../types";

type CloudinaryUploadWidgetProps = {
  onUploadSuccess: (url: string) => void;
};

type Cloudinary = {
  createUploadWidget: (
    options: {
      cloudName: string;
      apiKey: string;
      uploadSignature: string;
      uploadSignatureTimestamp: string;
      uploadPreset: string;
    },
    callback: (
      error: unknown,
      result: {
        event: string;
        info: { secure_url: string };
      },
    ) => void,
  ) => { open: () => void };
};

const CloudinaryUploadWidget = ({
  onUploadSuccess,
}: CloudinaryUploadWidgetProps) => {
  const cloudinaryRef = useRef<Cloudinary | null>(null);
  const widgetRef = useRef<{ open: () => void } | null>(null);

  useEffect(() => {
    cloudinaryRef.current = (
      window as unknown as {
        cloudinary: Cloudinary;
      }
    ).cloudinary;

    if (!cloudinaryRef.current) {
      console.error("Cloudinary is not available on the window object");
      return;
    }

    const fetchSignature = async () => {
      try {
        const response = await fetch("/api/cloudinary/signature");
        if (!response.ok) {
          throw new Error("Failed to fetch signature");
        }
        const data: SignatureResponse =
          (await response.json()) as SignatureResponse;

        if (cloudinaryRef.current) {
          widgetRef.current = cloudinaryRef.current.createUploadWidget(
            {
              cloudName: data.cloudName,
              apiKey: data.apiKey,
              uploadSignature: data.signature,
              uploadSignatureTimestamp: data.timestamp,
              uploadPreset: data.uploadPreset,
            },
            (error, result) => {
              if (!error && result && result.event === "success") {
                onUploadSuccess(result.info.secure_url);
              }
            },
          );
        }
      } catch (error) {
        console.error("Error setting up the upload widget", error);
      }
    };

    void fetchSignature();
  }, [onUploadSuccess]);

  const handleUploadClick = () => {
    if (widgetRef.current) {
      widgetRef.current.open();
    } else {
      console.error("Widget is not initialized");
    }
  };

  return (
    <div style={{ display: "flex", justifyContent: "center" }}>
      <button
        onClick={handleUploadClick}
        type="button"
        className="mt-4 px-4 py-2 bg-blue-500 text-white rounded-lg disabled:opacity-50"
      >
        Upload Image
      </button>
    </div>
  );
};

export default CloudinaryUploadWidget;
