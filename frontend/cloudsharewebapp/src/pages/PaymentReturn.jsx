import { useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '@clerk/clerk-react'
import axios from 'axios'
import { apiEndpoints } from '../util/apiEndPoints'

const PaymentReturn = () => {
    const navigate = useNavigate()
    const { getToken } = useAuth()

    useEffect(() => {
        const verify = async () => {
            const params = Object.fromEntries(new URLSearchParams(window.location.search))
            const orderInfo = params.vnp_OrderInfo || ''
            const planId = orderInfo.includes('premium') ? 'premium'
                : orderInfo.includes('ultimate') ? 'ultimate' : ''

            const payload = { ...params, planId } 
            try {
                const token = await getToken()
                const response = await axios.post(apiEndpoints.VERIFY_PAYMENT, payload, {
                    headers: { 'Authorization': `Bearer ${token}` }
                })
                console.log('Verify response:', response.data) // ← thêm

                if (response.data.success) {
                    navigate(`/subscription?payment=success&credits=${response.data.credits}`)
                } else {
                    navigate('/subscription?payment=failed')
                }
                // eslint-disable-next-line no-unused-vars
            } catch (error) {
                navigate('/subscription?payment=failed')
            }
        }

        verify()
    }, [])

    return (
        <div className="flex items-center justify-center h-screen">
            <p className="text-gray-600">Đang xử lý thanh toán...</p>
        </div>
    )
}

export default PaymentReturn