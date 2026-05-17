import React from 'react';
import { File, Clock, UploadCloud, Eye, Lock, Globe } from 'lucide-react';

const RecentFiles = ({ files }) => {
  const formatFileSize = (bytes) => {
    if (bytes === 0) return '0 KB';
    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(1)) + ' ' + sizes[i];
  };

  return (
    <div className="bg-white rounded-2xl shadow-lg border border-gray-100 overflow-hidden">
      {/* Header */}
      <div className="bg-gradient-to-r from-purple-600 via-indigo-600 to-purple-700 px-6 py-6">
        <div className="flex items-center gap-3">
          <Clock size={28} className="text-white" />
          <div>
            <h2 className="text-2xl font-bold text-white">Recent Files</h2>
            <p className="text-purple-100 text-sm mt-1">Your 5 most recent uploads</p>
          </div>
        </div>
      </div>

      {/* Content */}
      <div className="p-6">
        {files.length === 0 ? (
          <div className="text-center py-16 px-4">
            <div className="w-24 h-24 bg-gradient-to-br from-gray-100 to-gray-200 rounded-2xl flex items-center justify-center mx-auto mb-6">
              <UploadCloud size={48} className="text-gray-400" />
            </div>
            <h3 className="text-xl font-semibold text-gray-900 mb-3">No files yet</h3>
            <p className="text-gray-500 max-w-sm mx-auto mb-6">
              Upload your first file using the upload section to see your recent activity here.
            </p>
            <div className="w-24 h-12 bg-gradient-to-r from-purple-600 to-indigo-600 rounded-xl flex items-center justify-center mx-auto text-white font-medium">
              Start Uploading
            </div>
          </div>
        ) : (
          <div className="space-y-3">
            {files.map((file) => (
              <div 
                key={file.id} 
                className="group flex items-center p-4 hover:bg-gradient-to-r hover:from-purple-50 hover:to-indigo-50 rounded-2xl transition-all duration-300 cursor-pointer border border-gray-100 hover:border-purple-200 hover:shadow-md"
              >
                {/* File icon */}
                <div className="w-12 h-12 bg-gradient-to-br from-purple-500 to-indigo-500 rounded-xl flex items-center justify-center flex-shrink-0 shadow-lg">
                  <File size={20} className="text-white" />
                </div>

                {/* File info */}
                <div className="ml-4 flex-1 min-w-0">
                  <p className="font-semibold text-gray-900 text-sm truncate pr-4" title={file.name}>
                    {file.name}
                  </p>
                  <div className="flex items-center gap-4 text-xs text-gray-500 mt-1">
                    <span>{formatFileSize(file.size)}</span>
                    <span>•</span>
                    <span>{new Date(file.uploadedAt).toLocaleDateString('en-US', { 
                      year: 'numeric', 
                      month: 'short', 
                      day: 'numeric' 
                    })}</span>
                  </div>
                </div>

                {/* Status badge */}
                <div className="flex items-center gap-3 ml-auto">
                  {file.isPublic ? (
                    <div className="flex items-center gap-1 px-3 py-1.5 bg-green-100 border border-green-200 rounded-full text-xs font-medium text-green-800 shadow-sm">
                      <Globe size={14} />
                      <span>Public</span>
                    </div>
                  ) : (
                    <div className="flex items-center gap-1 px-3 py-1.5 bg-gray-100 border border-gray-200 rounded-full text-xs font-medium text-gray-700 shadow-sm">
                      <Lock size={14} />
                      <span>Private</span>
                    </div>
                  )}
                  
                  <div className="p-2 text-gray-400 group-hover:text-purple-600 transition-colors rounded-lg hover:bg-purple-100">
                    <Eye size={18} />
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default RecentFiles;