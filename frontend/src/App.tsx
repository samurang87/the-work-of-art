import './App.css'
import Header from "./components/Header.tsx";
import Footer from "./components/Footer.tsx";

function App() {
    return (
        <div className="min-h-screen bg-gray-50">
            <Header />
            <main className="container mx-auto px-4 py-8 mt-16">
                <h1>Henlo world</h1>
            </main>
            <Footer />
        </div>
    );
}
export default App
