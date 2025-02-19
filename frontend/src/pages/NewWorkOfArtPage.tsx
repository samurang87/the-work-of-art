import WorkOfArtForm from "../components/WorkOfArtForm.tsx";
import { useNavigate } from "react-router-dom";
import PageContainer from "../components/PageContainer.tsx";

export default function NewWorkOfArtPage({
  userId,
  username,
}: {
  userId: string | undefined;
  username: string | undefined;
}) {
  const navigate = useNavigate();

  return (
    <PageContainer>
      <WorkOfArtForm
        user={userId}
        userName={username}
        onSuccess={() => {
          void navigate("/feed");
        }}
      />
    </PageContainer>
  );
}
