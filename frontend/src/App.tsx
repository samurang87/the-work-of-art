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

function App() {
  const [username, setUsername] = useState<string | undefined>();
  const navigate = useNavigate();

  async function loadUser() {
    try {
      const response = await axios.get<string>("/api/auth/me");
      setUsername(response.data);
    } catch (error) {
      console.error("Failed to load user", error);
    }
  }

  function logout() {
    localStorage.removeItem("token");
    setUsername(undefined);
    void navigate("/");
  }

  useEffect(() => {
    void loadUser();
  }, []);

  return (
    <div className="min-h-screen bg-gray-50">
      <Header logout={logout} />
      <main className="container mx-auto px-4 py-8 mt-16">
        <Routes>
          <Route path="/" element={<LandingPage username={username} />} />
          <Route element={<ProtectedRoutes username={username} />}>
            <Route path="/feed" element={<Feed />} />
            <Route path="/user/:username?" element={<ProfilePage />} />
            <Route path="/woa/:workOfArtId?" element={<WorkOfArtPage />} />
          </Route>
        </Routes>
      </main>
      <Footer />
    </div>
  );
}

export default App;
