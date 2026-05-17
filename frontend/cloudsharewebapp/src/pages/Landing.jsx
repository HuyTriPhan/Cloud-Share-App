import React, { useEffect } from 'react'
import HeroSection from '../component/landing/HeroSection'
import FeaturesSection from '../component/landing/FeaturesSection'
import PricingSection from '../component/landing/PricingSection'
import TestimonialsSection from '../component/landing/TestimonialsSection'
import CTASection from '../component/landing/CTASection'
import Footer from '../component/landing/Footer'
import { features, pricingPlans, testimonials } from '../assets/data'
import { useNavigate } from 'react-router-dom'
import { useClerk, useUser } from '@clerk/clerk-react'

const Landing = () => {
    const { openSignIn, openSignUp } = useClerk();
    const { isSignedIn } = useUser();
    const navigate = useNavigate();

    useEffect(() => {
        if (isSignedIn) {
            navigate("/dashboard");
        }
    }, [isSignedIn, navigate]);

    return (
        <div className="landing-page bg-linear-to-b from-gray-50 to-gray-100">
            {/* Hero Section*/}
            <HeroSection openSignIn={openSignIn} openSignUp={openSignUp} />

            {/* Features section*/}
            <FeaturesSection features={features} />

            {/* Pricing section*/}
            <PricingSection pricingPlans={pricingPlans} openSignUp={openSignUp} />

            {/* Testimonials section*/}
            <TestimonialsSection testimonials={testimonials} />

            {/* CTA section*/}
            <CTASection openSignUp={openSignUp} />

            {/* Footer section*/}
            <Footer />
        </div>
    )
}

export default Landing