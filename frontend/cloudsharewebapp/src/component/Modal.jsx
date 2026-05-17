import React, { useEffect } from 'react';
import { X } from 'lucide-react';

const Modal = ({
  isOpen,
  onClose,
  title = "",
  children,
  size = "md", // sm, md, lg, xl
  confirmText = "Confirm",
  cancelText = "Cancel",
  onConfirm,
  confirmationButtonClass = "bg-red-600 hover:bg-red-700",
  showFooter = true
}) => {
  // Đóng modal khi click ESC
  useEffect(() => {
    const handleEscape = (e) => {
      if (e.key === 'Escape' && isOpen) {
        onClose();
      }
    };
    if (isOpen) {
      document.addEventListener('keydown', handleEscape);
      document.body.style.overflow = 'hidden'; // Ngăn scroll background
    }
    return () => {
      document.removeEventListener('keydown', handleEscape);
      document.body.style.overflow = 'unset';
    };
  }, [isOpen, onClose]);

  if (!isOpen) return null;

  // Size classes
  const sizeClasses = {
    sm: 'max-w-sm',
    md: 'max-w-md',
    lg: 'max-w-lg',
    xl: 'max-w-2xl'
  };

  const handleBackdropClick = (e) => {
    if (e.target === e.currentTarget) {
      onClose();
    }
  };

  return (
    <div 
      className="fixed inset-0 z-50 overflow-y-auto bg-black/50 backdrop-blur-sm flex items-center justify-center p-4"
      onClick={handleBackdropClick}
    >
      <div className={`bg-white rounded-2xl shadow-2xl max-h-[90vh] overflow-y-auto transform transition-all duration-300 scale-100 ${sizeClasses[size]}`}>
        {/* Header */}
        <div className="sticky top-0 z-10 bg-white border-b border-gray-100 rounded-t-2xl p-6">
          <div className="flex items-center justify-between">
            <h2 className="text-xl font-bold text-gray-900">
              {title}
            </h2>
            <button
              onClick={onClose}
              className="p-1.5 text-gray-400 hover:text-gray-600 hover:bg-gray-100 rounded-lg transition-all duration-200"
              aria-label="Close modal"
            >
              <X size={24} />
            </button>
          </div>
        </div>

        {/* Body */}
        <div className="p-6">
          <div className="prose prose-sm max-w-none">
            {children}
          </div>
        </div>

        {/* Footer */}
        {showFooter && (
          <div className="sticky bottom-0 z-10 bg-white border-t border-gray-100 rounded-b-2xl p-6 pt-0 flex justify-end gap-3">
            <button
              onClick={onClose}
              className="px-4 py-2 text-gray-700 bg-gray-100 hover:bg-gray-200 rounded-xl transition-colors duration-200 font-medium"
            >
              {cancelText}
            </button>
            {onConfirm && (
              <button
                onClick={onConfirm}
                className={`px-4 py-2 text-white font-medium rounded-xl transition-colors duration-200 shadow-lg hover:shadow-xl transform hover:-translate-y-0.5 ${confirmationButtonClass}`}
              >
                {confirmText}
              </button>
            )}
          </div>
        )}
      </div>
    </div>
  );
};

export default Modal;