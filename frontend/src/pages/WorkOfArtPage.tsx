import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import WorkOfArtComponent from "../components/WorkOfArtComponent.tsx";
import { WorkOfArt } from "../types.tsx";

export default function WorkOfArtPage() {
  const { workOfArtId } = useParams<{ workOfArtId?: string }>();
  const [workOfArt, setWorkOfArt] = useState<WorkOfArt | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    async function fetchWorkOfArt(workOfArtId: string | undefined) {
      if (!workOfArtId) {
        setError("Work of art ID is required");
        setLoading(false);
        return;
      }
      setLoading(true);
      try {
        const response = await axios.get<WorkOfArt>("/api/woa/" + workOfArtId);
        setWorkOfArt(response.data);
        setError(null);
      } catch (error) {
        setError(error instanceof Error ? error.message : String(error));
      } finally {
        setLoading(false);
      }
    }

    void fetchWorkOfArt(workOfArtId);
  }, [workOfArtId]);

  if (loading) return <div className="mt-24 text-center">Loading...</div>;
  if (error)
    return <div className="mt-24 text-center text-red-500">Error: {error}</div>;
  if (!workOfArt)
    return (
      <div className="mt-24 text-center">No work of art data available</div>
    );

  return (
    <div className="pb-24">
      <WorkOfArtComponent workOfArt={workOfArt} />
    </div>
  );
}
