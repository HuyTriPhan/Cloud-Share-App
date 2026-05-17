import React from 'react'
import { assets } from '../../assets/assets'

const HeroSection = ({ openSignIn, openSignUp }) => {
    return (
        <div className="landing-page-content relative">
            <div className="absolute inset-0 bg-linear-to-r from-purple-50 to-indigo-50 opacity-80 z-0 pointer-events-none"></div>
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 relative z-10">
                <div className="pt-20 pb-16 sm:pt-24 sm:pb-20 lg:pt-32 lg:pb-28">
                    <div className="text-center">
                        <h1 className="text-4xl tracking-tight font-extrabold text-gray-900 sm:text-5xl md:text-6xl">
                            <span className="block">Share Files Securely with</span>
                            <span className="block text-purple-500">CloudShare</span>
                        </h1>
                        <div className="mt-10 max-w-sm mx-auto sm:max-w-none sm:flex sm:justify-center">
                            <div className="space-y-4 sm:space-y-0 sm:mx-auto sm:inline-grid sm:grid-cols-2 sm:gap-5">
                                <button
                                    onClick={() => openSignUp()}
                                    className="flex items-center px-6 py-3 border border-transparent text-base font-medium rounded-md text-white bg-purple-500 hover:bg-purple-600 md:py-4 md:text-lg md:px-10 transition-all duration-200 shadow-lg hover:shadow-xl">Get Started</button>
                                <button
                                    onClick={() => openSignIn()}
                                    className="flex items-center justify-center px-6 py-3 border border-gray-700 bg-white hover:bg-gray-50 md:py-4 md:text-lg md:px-10 transition-all duration-200 shadow-md hover:shadow-lg">Sign In</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>




        </div>
    )
}

export default HeroSection