import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import WorkOfArtComponent from "../components/WorkOfArtComponent.tsx";
import WorkOfArtForm from "../components/WorkOfArtForm.tsx";
import { WorkOfArt } from "../types.tsx";

type WorkOfArtPageProps = {
  loggedInUsername: string | undefined;
};

export default function WorkOfArtPage({
  loggedInUsername,
}: WorkOfArtPageProps) {
  const { workOfArtId } = useParams<{ workOfArtId?: string }>();
  const [workOfArt, setWorkOfArt] = useState<WorkOfArt | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [isEditMode, setIsEditMode] = useState(false);

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

  const handleSuccess = () => {
    setIsEditMode(false);
    window.location.href = `/woa/${workOfArtId}`;
  };

  if (loading) return <div className="mt-24 text-center">Loading...</div>;
  if (error)
    return <div className="mt-24 text-center text-red-500">Error: {error}</div>;
  if (!workOfArt)
    return (
      <div className="mt-24 text-center">No work of art data available</div>
    );

  const isOwnProfile = workOfArt.userName === loggedInUsername;

  return (
    <div className="pb-24">
      {isEditMode ? (
        <WorkOfArtForm
          user={workOfArt.user}
          userName={workOfArt.userName}
          onSuccess={handleSuccess}
          initialData={{
            ...workOfArt,
            materials: workOfArt.materials || [],
          }}
          isEditMode={true}
          workOfArtId={workOfArtId}
        />
      ) : (
        <>
          {isOwnProfile && (
            <div style={{ display: "flex", justifyContent: "center" }}>
              <button
                onClick={() => setIsEditMode(true)}
                className="mt-4 px-4 py-2 bg-blue-500 text-white rounded-lg"
              >
                Edit
              </button>
            </div>
          )}
          <WorkOfArtComponent workOfArt={workOfArt} />
        </>
      )}
    </div>
  );
}
