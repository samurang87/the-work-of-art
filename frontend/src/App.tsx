import './App.css'
import Header from "./components/Header.tsx";
import Footer from "./components/Footer.tsx";
import {Route, Routes} from "react-router-dom";
import Feed from "./pages/Feed.tsx";

function App() {
    return (
        <div className="min-h-screen bg-gray-50">
            <Header />
            <main className="container mx-auto px-4 py-8 mt-16">
                <Routes>
                    <Route path={"/"} element={<Feed/>} />
                    <Route path={"/feed"} element={<Feed/>} />
                </Routes>
            </main>
            <Footer />
        </div>
    );
}
export default App
