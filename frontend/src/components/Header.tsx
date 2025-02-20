import { MouseEvent } from "react";

type HeaderProps = {
  logout: () => void;
  username: string | undefined;
};

export default function Header({ logout, username }: HeaderProps) {
  const navLinks = [
    { name: "Feed", path: "/feed" },
    { name: "Profile", path: `/user/${username}` },
    { name: "New WoA", path: "/woa/new" },
    {
      name: "Logout",
      path: "#",
      onClick: (e: MouseEvent<HTMLAnchorElement>) => {
        e.preventDefault();
        logout();
      },
    },
  ];

  return (
    <header className="fixed top-0 left-0 right-0 bg-white/80 backdrop-blur-sm shadow-sm z-10">
      <div className="container mx-auto px-4">
        <div className="flex justify-between items-center h-20">
          {/* Logo */}
          <a href="/feed" className="flex items-center">
            <img
              src="/logo_temp.png"
              alt="App Logo"
              className="w-20 h-20 object-contain"
            />
          </a>

          {/* Navigation */}
          {username && (
            <>
              <nav className="hidden md:flex space-x-8">
                {navLinks.map((link) => (
                  <a
                    key={link.name}
                    href={link.path}
                    className="text-gray-600 hover:text-gray-900 transition-colors"
                    onClick={link.onClick}
                  >
                    {link.name}
                  </a>
                ))}
              </nav>

              {/* Mobile Menu Button */}
              <button className="md:hidden">
                <svg
                  className="w-6 h-6"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M4 6h16M4 12h16M4 18h16"
                  />
                </svg>
              </button>
            </>
          )}
        </div>
      </div>
    </header>
  );
}
