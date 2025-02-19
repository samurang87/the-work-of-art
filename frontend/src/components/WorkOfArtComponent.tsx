import { WorkOfArt } from "../types.tsx";

type WorkOfArtComponentProps = {
  workOfArt: WorkOfArt;
  allFields?: boolean;
};

function transformImageUrl(url: string): string {
  const parts = url.split("/upload/");
  if (parts.length !== 2) {
    throw new Error("Invalid URL format");
  }
  return `${parts[0]}/upload/c_scale,w_500/q_auto/f_auto/${parts[1]}`;
}

function formatDate(dateString: string): string {
  const date = new Date(dateString);
  const day = String(date.getDate()).padStart(2, "0");
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const year = date.getFullYear();
  return `${day}/${month}/${year}`;
}

export default function WorkOfArtComponent({
  workOfArt,
  allFields = true,
}: WorkOfArtComponentProps) {
  return (
    <div className="container mx-auto px-4 mt-24">
      <div className="max-w-2xl mx-auto space-y-6">
        <div className="bg-white/80 backdrop-blur-sm shadow-sm rounded-lg overflow-hidden">
          {/* Title and medium */}
          <div className="flex justify-between p-6">
            <h3 className="text-2xl font-semibold text-gray-800">
              <a href={`/woa/${workOfArt.id}`}>{workOfArt.title}</a>
            </h3>

            <p className="text-gray-600">{workOfArt.medium}</p>
          </div>
          {/* Image */}
          <img
            src={transformImageUrl(workOfArt.imageUrl)}
            alt={workOfArt.title}
            className="w-full"
          />
          {/* User and date */}
          <div className="flex justify-between p-6">
            <h2 className="text-2xl font-semibold text-gray-800">
              <a href={`/user/${workOfArt.userName}`}>{workOfArt.userName}</a>
            </h2>
            <p className="text-gray-600">{formatDate(workOfArt.createdAt)}</p>
          </div>
        </div>

        {allFields && (
          <>
            {/* Description Section */}
            <div className="bg-white/80 backdrop-blur-sm shadow-sm rounded-lg p-6">
              <h3 className="text-xl font-semibold text-gray-800 mb-4">
                Description
              </h3>
              <p className="text-gray-600">
                {workOfArt.description || "No description yet 🌳"}
              </p>
            </div>

            {/* Materials Section */}
            {workOfArt.materials && workOfArt.materials.length > 0 && (
              <div className="bg-white/80 backdrop-blur-sm shadow-sm rounded-lg p-6">
                <h3 className="text-xl font-semibold text-gray-800 mb-4">
                  Materials
                </h3>
                {workOfArt.materials.map((material, index) => (
                  <p key={index} className="text-gray-600">
                    {material.type} {material.identifier} - {material.name} by{" "}
                    {material.brand} ({material.line})
                  </p>
                ))}
              </div>
            )}

            {/* Challenge Section */}
            {workOfArt.challengeId && (
              <div className="bg-white/80 backdrop-blur-sm shadow-sm rounded-lg p-6">
                <h3 className="text-xl font-semibold text-gray-800 mb-4">
                  Challenge
                </h3>
                <p className="text-gray-600">
                  See challenge: {workOfArt.challengeId}
                </p>
              </div>
            )}
          </>
        )}
      </div>
    </div>
  );
}
