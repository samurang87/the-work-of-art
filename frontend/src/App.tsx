import "./App.css";
import Header from "./components/Header.tsx";
import Footer from "./components/Footer.tsx";
import { Route, Routes, useNavigate } from "react-router-dom";
import Feed from "./pages/Feed.tsx";
import ProfilePage from "./pages/ProfilePage.tsx";
import WorkOfArtPage from "./pages/WorkOfArtPage.tsx";
import LandingPage from "./pages/LandingPage.tsx";
import ProtectedRoutes from "./components/ProtectedRoute.tsx";
import { useEffect, useState } from "react";
import axios from "axios";
import NewWorkOfArtPage from "./pages/NewWorkOfArtPage.tsx";

function App() {
  const [loggedInUsername, setLoggedInUsername] = useState<
    string | undefined
  >();
  const [loggedInUserId, setLoggedInUserId] = useState<string | undefined>();
  const navigate = useNavigate();

  async function loadUser() {
    try {
      const response = await axios.get<{ user: string; name: string }>(
        "/api/auth/me",
      );
      setLoggedInUsername(response.data.name);
      setLoggedInUserId(response.data.user);
    } catch (error) {
      console.error("Failed to load user", error);
    }
  }

  function logout() {
    localStorage.removeItem("token");
    setLoggedInUsername(undefined);
    setLoggedInUserId(undefined);
    void navigate("/");
  }

  useEffect(() => {
    void loadUser();
  }, []);

  return (
    <div className="min-h-screen bg-gray-50">
      <Header logout={logout} username={loggedInUsername} />
      <main className="container mx-auto px-4 py-8 mt-16">
        <Routes>
          <Route
            path="/"
            element={<LandingPage username={loggedInUsername} />}
          />
          <Route element={<ProtectedRoutes username={loggedInUsername} />}>
            <Route path="/feed" element={<Feed />} />
            <Route
              path="/user/:username?"
              element={<ProfilePage loggedInUsername={loggedInUsername} />}
            />
            <Route
              path="/woa/:workOfArtId?"
              element={<WorkOfArtPage loggedInUsername={loggedInUsername} />}
            />
            <Route
              path="/woa/new"
              element={
                <NewWorkOfArtPage
                  userId={loggedInUserId}
                  username={loggedInUsername}
                />
              }
            />
          </Route>
        </Routes>
      </main>
      <Footer />
    </div>
  );
}

export default App;
