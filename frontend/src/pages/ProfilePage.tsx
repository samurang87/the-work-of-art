import UserProfile from "../components/ProfileComponent.tsx";
import { useParams } from "react-router-dom";

type ProfilePageProps = {
  username: string | undefined;
};

export default function ProfilePage({ username }: ProfilePageProps) {
  const { username: paramUsername } = useParams<{ username: string }>();
  const displayUsername = paramUsername || username;

  if (!displayUsername) {
    return <div>User not found</div>;
  }

  return (
    <div className="container mx-auto px-4 mt-24 pb-24">
      <UserProfile username={displayUsername} />
    </div>
  );
}
