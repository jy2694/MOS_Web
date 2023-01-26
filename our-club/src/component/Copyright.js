import React from 'react'
import "../css/Copyright.css"
function Copyright() {
    return (
        <div className='d-flex align-items-center justify-content-center w-100 bg-white bg-opacity-25'

            style={{
                height: 10 + 'vh',
                position: 'fixed',
                bottom: 0
            }}>
            <span>Copyright 2023. Wku_Mos All rights reserved.</span>
        </div>
    )
}

export default Copyright