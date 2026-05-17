import React from 'react'
import {useUser} from "@clerk/clerk-react"
import Navbar from '../component/Navbar'
import SideMeun from '../component/SideMeun';

const DashboardLayout = ({children, activeMeun}) => {
    const {user} = useUser();
  return (
    <div>
        <Navbar activeMeun={activeMeun}/>
        {user && (
            <div className="flex">
                <div className="max-[1080px]:hidden">
                    <SideMeun activeMeun={activeMeun}/>
                </div>
                <div className="grow mx-5">{children}</div>
            </div>
        )}
    </div>
  )
}

export default DashboardLayout