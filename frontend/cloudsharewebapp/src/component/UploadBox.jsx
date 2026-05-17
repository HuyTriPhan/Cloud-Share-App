import React from "react";
import { Upload, FileText, X } from "lucide-react";

const UploadBox = ({
  files,
  onFileChange,
  onUpload,
  uploading,
  onRemoveFile,
  remainingCredits,
  isUploadDisabled,
}) => {

  const formatFileSize = (bytes) => {
    if (bytes < 1024) return bytes + " B";
    if (bytes < 1024 * 1024)
      return (bytes / 1024).toFixed(2) + " KB";

    return (bytes / (1024 * 1024)).toFixed(2) + " MB";
  };

  return (
    <div className="w-full max-w-4xl mx-auto">

      {/* Header */}
      <div className="flex items-center justify-between mb-3">
        <div className="flex items-center gap-2">
          <Upload className="text-blue-600" size={20} />
          <h2 className="text-lg font-semibold">Upload Files</h2>
        </div>

        <p className="text-sm text-gray-500">
          {remainingCredits} credits remaining
        </p>
      </div>

      {/* Upload Area */}
      <label className="border-2 border-dashed border-gray-300 rounded-xl h-64 flex flex-col items-center justify-center cursor-pointer hover:border-blue-400 transition bg-white">
        <div className="w-14 h-14 rounded-full bg-blue-100 flex items-center justify-center mb-4">
          <Upload className="text-blue-600" size={28} />
        </div>

        <p className="text-lg font-medium text-gray-700">
          Drag and drop files here
        </p>

        <p className="text-sm text-gray-500 mt-1">
          or click to browse ({remainingCredits} credits remaining)
        </p>

        <input
          type="file"
          multiple
          className="hidden"
          onChange={onFileChange}
        />
      </label>

      {/* Selected Files */}
      {files.length > 0 && (
        <div className="mt-6">
          <h3 className="font-semibold text-gray-700 mb-3">
            Selected Files ({files.length})
          </h3>

          <div className="border border-gray-200 rounded-xl overflow-hidden">
            {files.map((file, index) => (
              <div
                key={index}
                className="flex items-center justify-between px-4 py-4 border-b last:border-b-0 bg-white"
              >
                <div className="flex items-center gap-3">
                  <FileText className="text-blue-600" size={20} />

                  <div>
                    <p className="text-sm font-medium text-gray-800">
                      {file.name}
                    </p>

                    <p className="text-xs text-gray-500">
                      {formatFileSize(file.size)}
                    </p>
                  </div>
                </div>

                <button
                  onClick={() => onRemoveFile(index)}
                  className="text-gray-400 hover:text-red-500 transition"
                >
                  <X size={18} />
                </button>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* Upload Button */}
      <button
        onClick={onUpload}
        disabled={isUploadDisabled()}
        className={`w-full mt-6 py-4 rounded-xl text-white font-medium transition
          ${
            isUploadDisabled()
              ? "bg-blue-300 cursor-not-allowed"
              : "bg-blue-600 hover:bg-blue-700"
          }`}
      >
        {uploading ? "Uploading..." : "Upload"}
      </button>
    </div>
  );
};

export default UploadBox;