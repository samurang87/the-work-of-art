import { useEffect, useState } from "react";
import axios from "axios";
import WorkOfArtComponent from "../components/WorkOfArtComponent";
import { WorkOfArt } from "../types.tsx";
import PageContainer from "../components/PageContainer.tsx";

export default function Feed() {
  const [worksOfArt, setWorksOfArt] = useState<WorkOfArt[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    async function fetchWorksOfArt() {
      try {
        const response = await axios.get<WorkOfArt[]>("/api/woa");
        setWorksOfArt(response.data);
        setError(null);
      } catch (error) {
        setError(error instanceof Error ? error.message : "An error occurred");
      } finally {
        setLoading(false);
      }
    }

    void fetchWorksOfArt();
  }, []);

  if (loading)
    return (
      <PageContainer>
        <div className="mt-24 text-center">Loading...</div>
      </PageContainer>
    );
  if (error)
    return (
      <PageContainer>
        <div className="mt-24 text-center text-red-500">Error: {error}</div>
      </PageContainer>
    );

  return (
    <PageContainer>
      <div className="max-w-2xl mx-auto space-y-6">
        {worksOfArt.map((workOfArt) => (
          <WorkOfArtComponent
            key={workOfArt.id}
            workOfArt={workOfArt}
            allFields={false}
          />
        ))}
      </div>
    </PageContainer>
  );
}
