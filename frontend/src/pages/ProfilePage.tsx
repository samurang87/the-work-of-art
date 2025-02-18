import UserProfile from "../components/ProfileComponent.tsx";
import { useParams } from "react-router-dom";
import PageContainer from "../components/PageContainer.tsx";

type ProfilePageProps = {
  loggedInUsername: string | undefined;
};

export default function ProfilePage({ loggedInUsername }: ProfilePageProps) {
  const { username } = useParams<{ username: string }>();

  if (!username) {
    return (
      <PageContainer>
        <div>User not found</div>
      </PageContainer>
    );
  }

  return (
    <PageContainer>
      <UserProfile username={username} loggedInUsername={loggedInUsername} />
    </PageContainer>
  );
}
