import WorkOfArtForm from "../components/WorkOfArtForm.tsx";
import { useNavigate } from "react-router-dom";

export default function NewWorkOfArtPage({
  userId,
  username,
}: {
  userId: string | undefined;
  username: string | undefined;
}) {
  const navigate = useNavigate();

  return (
    <div className="container mx-auto px-4 mt-24 pb-24">
      <WorkOfArtForm
        user={userId}
        userName={username}
        onSuccess={() => {
          void navigate("/feed");
        }}
      />
    </div>
  );
}
