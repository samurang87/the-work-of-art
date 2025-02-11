import { FormEvent, useEffect, useState } from "react";
import {
  Medium,
  WorkOfArtCreateRequest,
  MaterialCreateRequest,
} from "../types.tsx";

type WorkOfArtFormProps = {
  user: string | undefined;
  userName: string | undefined;
  onSuccess?: () => void;
};

const MaterialForm = ({
  index,
  material,
  onChange,
  onRemove,
}: {
  index: number;
  material: MaterialCreateRequest;
  onChange: (index: number, material: MaterialCreateRequest) => void;
  onRemove: (index: number) => void;
}) => (
  <div className="bg-white/80 backdrop-blur-sm shadow-sm rounded-lg p-6 space-y-4">
    <div className="flex justify-between items-center">
      <h3 className="text-lg font-medium">Material {index + 1}</h3>
      {index > 0 && (
        <button
          type="button"
          onClick={() => onRemove(index)}
          className="text-red-500 hover:text-red-700"
        >
          Remove
        </button>
      )}
    </div>
    <input
      type="text"
      placeholder="Material Name"
      value={material.name}
      onChange={(e) => onChange(index, { ...material, name: e.target.value })}
      className="w-full p-2 border rounded"
      required
    />
    <input
      type="text"
      placeholder="Identifier (optional)"
      value={material.identifier || ""}
      onChange={(e) =>
        onChange(index, {
          ...material,
          identifier: e.target.value || null,
        })
      }
      className="w-full p-2 border rounded"
    />
    <input
      type="text"
      placeholder="Brand (optional)"
      value={material.brand || ""}
      onChange={(e) =>
        onChange(index, {
          ...material,
          brand: e.target.value || null,
        })
      }
      className="w-full p-2 border rounded"
    />
    <input
      type="text"
      placeholder="Line (optional)"
      value={material.line || ""}
      onChange={(e) =>
        onChange(index, {
          ...material,
          line: e.target.value || null,
        })
      }
      className="w-full p-2 border rounded"
    />
    <input
      type="text"
      placeholder="Type (optional)"
      value={material.type || ""}
      onChange={(e) =>
        onChange(index, {
          ...material,
          type: e.target.value || null,
        })
      }
      className="w-full p-2 border rounded"
    />
    <select
      value={material.medium || ""}
      onChange={(e) =>
        onChange(index, {
          ...material,
          medium: e.target.value || null,
        })
      }
      className="w-full p-2 border rounded"
    >
      <option value="">Select Medium (optional)</option>
      {Object.values(Medium).map((medium) => (
        <option key={medium} value={medium}>
          {medium}
        </option>
      ))}
    </select>
  </div>
);

export default function WorkOfArtForm({
  user,
  userName,
  onSuccess,
}: WorkOfArtFormProps) {
  const [formData, setFormData] = useState<Partial<WorkOfArtCreateRequest>>({
    user,
    userName,
    materials: [
      {
        name: "",
        identifier: null,
        brand: null,
        line: null,
        type: null,
        medium: null,
      },
    ],
  });
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    setFormData((prevData) => ({
      ...prevData,
      user,
      userName,
    }));
  }, [user, userName]);

  const handleMaterialChange = (
    index: number,
    material: MaterialCreateRequest,
  ) => {
    setFormData((prev) => {
      const newMaterials = [...(prev.materials || [])];
      newMaterials[index] = material;
      return { ...prev, materials: newMaterials };
    });
  };

  const handleAddMaterial = () => {
    setFormData((prev) => ({
      ...prev,
      materials: [
        ...(prev.materials || []),
        {
          name: "",
          identifier: null,
          brand: null,
          line: null,
          type: null,
          medium: null,
        },
      ],
    }));
  };

  const handleRemoveMaterial = (index: number) => {
    setFormData((prev) => ({
      ...prev,
      materials: (prev.materials || []).filter((_, i) => i !== index),
    }));
  };

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setIsSubmitting(true);
    setError(null);

    try {
      const response = await fetch("/api/woa", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(formData),
      });

      if (!response.ok) {
        setError(`HTTP error! status: ${response.status}`);
        return;
      }

      onSuccess?.();
    } catch (err) {
      setError(err instanceof Error ? err.message : "An error occurred");
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="container mx-auto px-4 mt-24">
      <div className="max-w-2xl mx-auto space-y-6">
        <form
          onSubmit={(e) => {
            handleSubmit(e).catch(console.error);
          }}
        >
          {/* Title Section */}
          <div className="bg-white/80 backdrop-blur-sm shadow-sm rounded-lg p-6">
            <input
              type="text"
              placeholder="Title"
              value={formData.title || ""}
              onChange={(e) =>
                setFormData({
                  ...formData,
                  title: e.target.value,
                })
              }
              className="text-2xl font-semibold text-gray-800 w-full"
              required
            />
          </div>

          {/* Medium Selection Dropdown */}
          <div className="bg-white/80 backdrop-blur-sm shadow-sm rounded-lg p-6">
            <select
              value={formData.medium || ""}
              onChange={(e) =>
                setFormData({
                  ...formData,
                  medium: e.target.value,
                })
              }
              className="w-full p-2 border rounded"
              required
            >
              <option value="">Select Medium</option>
              {Object.values(Medium).map((medium) => (
                <option key={medium} value={medium}>
                  {medium}
                </option>
              ))}
            </select>
          </div>

          {/* Description Section */}
          <div className="bg-white/80 backdrop-blur-sm shadow-sm rounded-lg p-6">
            <textarea
              placeholder="Description"
              value={formData.description || ""}
              onChange={(e) =>
                setFormData({
                  ...formData,
                  description: e.target.value,
                })
              }
              className="w-full text-gray-600"
              rows={4}
            />
          </div>

          {/* Image Upload Section */}
          <div className="bg-white/80 backdrop-blur-sm shadow-sm rounded-lg p-6">
            <input
              type="text"
              placeholder="Image URL"
              value={formData.imageUrl || ""}
              onChange={(e) =>
                setFormData({
                  ...formData,
                  imageUrl: e.target.value,
                })
              }
              className="w-full"
              required
            />
          </div>

          {/* Challenge ID Section */}
          <div className="bg-white/80 backdrop-blur-sm shadow-sm rounded-lg p-6">
            <input
              type="text"
              placeholder="Challenge ID (optional)"
              value={formData.challengeId || ""}
              onChange={(e) =>
                setFormData({
                  ...formData,
                  challengeId: e.target.value,
                })
              }
              className="w-full"
            />
          </div>

          {/* Materials Section */}
          <div className="space-y-4">
            {formData.materials?.map((material, index) => (
              <MaterialForm
                key={index}
                index={index}
                material={material}
                onChange={handleMaterialChange}
                onRemove={handleRemoveMaterial}
              />
            ))}
            <button
              type="button"
              onClick={handleAddMaterial}
              className="w-full p-2 bg-gray-100 hover:bg-gray-200 rounded-lg text-gray-600"
            >
              + Add Material
            </button>
          </div>

          {error && (
            <div className="text-red-500 p-4 bg-white/80 backdrop-blur-sm rounded-lg">
              {error}
            </div>
          )}

          <button
            type="submit"
            disabled={isSubmitting}
            className="mt-4 px-4 py-2 bg-blue-500 text-white rounded-lg disabled:opacity-50"
          >
            {isSubmitting ? "Creating..." : "Create Work of Art"}
          </button>
        </form>
      </div>
    </div>
  );
}
