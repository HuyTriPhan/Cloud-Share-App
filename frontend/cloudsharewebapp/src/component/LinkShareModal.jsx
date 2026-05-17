import React from 'react';
import Modal from './Modal.jsx';
import { Copy, Check } from 'lucide-react';

const LinkShareModal = ({ 
  isOpen, 
  onClose, 
  link, 
  title = "Share Link" 
}) => {
  const [isCopied, setIsCopied] = React.useState(false);

  const copyToClipboard = async () => {
    try {
      await navigator.clipboard.writeText(link);
      setIsCopied(true);
      setTimeout(() => setIsCopied(false), 2000);
    } catch (err) {
      console.error('Failed to copy: ', err);
    }
  };

  return (
    <Modal
      isOpen={isOpen}
      onClose={onClose}
      title={title}
      size="md"
    >
      <div className="space-y-4">
        {/* Share Link */}
        <div className="bg-gray-50 p-4 rounded-lg">
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Shareable Link
          </label>
          <div className="flex items-center space-x-2">
            <input
              type="text"
              className="flex-1 px-3 py-2 border border-gray-300 rounded-md bg-white focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
              value={link}
              readOnly
            />
            <button
              onClick={copyToClipboard}
              className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 flex items-center gap-2 transition-colors"
              title="Copy link"
            >
              {isCopied ? (
                <>
                  <Check size={16} />
                  Copied!
                </>
              ) : (
                <>
                  <Copy size={16} />
                  Copy
                </>
              )}
            </button>
          </div>
        </div>

        {/* Instructions */}
        <div className="bg-blue-50 border border-blue-200 rounded-lg p-4">
          <h4 className="font-medium text-blue-900 mb-2">How to share:</h4>
          <ul className="text-sm text-blue-800 space-y-1">
            <li>• Anyone with this link can view your file</li>
            <li>• Link expires when you delete the file</li>
            <li>• You can toggle public/private anytime</li>
          </ul>
        </div>
      </div>
    </Modal>
  );
};

export default LinkShareModal;