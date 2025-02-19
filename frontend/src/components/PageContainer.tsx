import { ReactNode } from "react";

type PageContainerProps = {
  children: ReactNode;
  className?: string;
};

export default function PageContainer({
  children,
  className = "",
}: PageContainerProps) {
  return (
    <div
      className={`relative z-0 container mx-auto px-4 mt-24 pb-24 ${className}`}
    >
      {children}
    </div>
  );
}
