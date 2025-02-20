import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import WorkOfArtComponent from "../components/WorkOfArtComponent.tsx";
import WorkOfArtForm from "../components/WorkOfArtForm.tsx";
import { WorkOfArt } from "../types.tsx";
import PageContainer from "../components/PageContainer.tsx";

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

  if (loading)
    return (
      <PageContainer>
        <div className="text-center">Loading...</div>
      </PageContainer>
    );
  if (error)
    return (
      <PageContainer>
        <div className="text-center text-red-500">Error: {error}</div>
      </PageContainer>
    );
  if (!workOfArt)
    return (
      <PageContainer>
        <div className="text-center">No work of art data available</div>
      </PageContainer>
    );

  const isOwnProfile = workOfArt.userName === loggedInUsername;

  return (
    <PageContainer>
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
          {isOwnProfile && !isEditMode && (
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
    </PageContainer>
  );
}
