import React from 'react';
import { Upload, X, File, Loader2 } from 'lucide-react';

const DashboardUpload = ({
  files,
  onFileChange,
  onUpload,
  uploading,
  onRemoveFile,
  remainingUploads
}) => {
  return (
    <div className="bg-white rounded-2xl shadow-lg border border-gray-100 p-6 space-y-6">
      {/* Header */}
      <div className="text-center pb-6 border-b border-gray-100">
        <div className="w-16 h-16 bg-gradient-to-br from-purple-500 to-indigo-500 rounded-2xl flex items-center justify-center mx-auto mb-4">
          <Upload size={28} className="text-white" />
        </div>
        <h2 className="text-2xl font-bold text-gray-900 mb-2">Upload Files</h2>
        <p className="text-gray-600">
          Drag & drop or click to select files (<span className="font-semibold text-purple-600">{remainingUploads}</span> remaining)
        </p>
      </div>

      {/* File input */}
      <div className="space-y-4">
        <label className="block w-full p-8 border-2 border-dashed border-gray-300 rounded-2xl text-center hover:border-purple-400 hover:bg-purple-50/50 transition-all duration-300 cursor-pointer group">
          <File size={48} className="mx-auto text-gray-400 group-hover:text-purple-500 mb-4 transition-colors" />
          <p className="text-lg font-medium text-gray-700 group-hover:text-purple-600 transition-colors">
            Click to select or drag & drop
          </p>
          <p className="text-sm text-gray-500 mt-1">PNG, JPG, MP4, PDF, up to 100MB</p>
          <input
            type="file"
            multiple
            onChange={onFileChange}
            className="hidden"
            accept="image/*,video/*,audio/*,.pdf,.doc,.docx,.txt"
          />
        </label>

        {/* File list */}
        {files.length > 0 && (
          <>
            <div className="flex items-center justify-between pt-4 border-t border-gray-100 pb-3">
              <h3 className="font-semibold text-gray-900">
                {files.length} file{files.length !== 1 ? 's' : ''} selected
              </h3>
              <span className="text-sm text-gray-500 font-medium">
                {remainingUploads} remaining
              </span>
            </div>
            <div className="space-y-2 max-h-48 overflow-y-auto -mx-6 px-6">
              {files.map((file, index) => (
                <div key={index} className="flex items-center justify-between p-3 bg-gray-50/50 rounded-xl group hover:bg-gray-100 transition-all duration-200 border border-gray-200">
                  <div className="flex items-center gap-3 truncate flex-1">
                    <div className="w-10 h-10 bg-gradient-to-br from-gray-400 to-gray-500 rounded-lg flex items-center justify-center flex-shrink-0">
                      <File size={16} className="text-white" />
                    </div>
                    <div className="truncate min-w-0">
                      <p className="font-medium text-gray-900 text-sm truncate" title={file.name}>
                        {file.name}
                      </p>
                      <p className="text-xs text-gray-500">
                        {(file.size / 1024).toFixed(1)} KB
                      </p>
                    </div>
                  </div>
                  <button
                    onClick={() => onRemoveFile(index)}
                    className="p-2 text-gray-400 hover:text-red-500 hover:bg-red-100 rounded-xl transition-all duration-200 ml-2"
                    title="Remove file"
                  >
                    <X size={18} />
                  </button>
                </div>
              ))}
            </div>
          </>
        )}
      </div>

      {/* Upload button */}
      <button
        onClick={onUpload}
        disabled={uploading || files.length === 0}
        className={`w-full py-4 px-6 rounded-2xl font-semibold text-lg transition-all duration-300 flex items-center justify-center gap-3 shadow-lg ${
          uploading || files.length === 0
            ? 'bg-gray-400 text-gray-500 cursor-not-allowed'
            : 'bg-gradient-to-r from-purple-600 to-indigo-600 text-white hover:from-purple-700 hover:to-indigo-700 hover:shadow-2xl hover:-translate-y-1 active:translate-y-0'
        }`}
      >
        {uploading ? (
          <>
            <Loader2 size={24} className="animate-spin" />
            Uploading...
          </>
        ) : (
          `Upload ${files.length || 0} File${files.length !== 1 ? 's' : ''}`
        )}
      </button>
    </div>
  );
};

export default DashboardUpload;