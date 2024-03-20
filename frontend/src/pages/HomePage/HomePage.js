import React, { useState, useEffect } from 'react';

import Wallet from './Wallet/Wallet';
import PresentationPage from './PresentationPage/PresentationPage';

function HomePage({isAuth}) {
    return <>{isAuth === true ? <Wallet /> : <PresentationPage />}</>;
}

export default HomePage;
