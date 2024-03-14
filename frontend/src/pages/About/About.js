import React from 'react'
import screen from '../../assets/img/bg-pc-resize-tiny.png'
import logoFac from '../../assets/img/logo-fac.svg'

function About() {
    return (
        <div className=" containerPage">
            <div className="d-flex align-center">
               <div className=" w-50 d-flex flex-column align-center mr-1-r">
                   <h1>Notre mission</h1>
                   <p>Alors la misssion de derieufhoie hdfuohg hgudfhog hgfudh gfhdi h hgiufdghufid ghiu hgfdiu hghg fidhgiu fd hgifh igfdh i dhigfd</p>
               </div>

                <img className="w-50" src={screen}/>
            </div>
            <div className="d-flex flex-column align-center mt-3-r mb-3-r">
                <h1>Notre histoire</h1>
                <div className="d-flex align-center w-100">

                    <img className="w-30 mr-1-r" src={logoFac}/>


                    <div className="w-70">
                        <p>fdsihfsdh gdos hiodf hihg iudfh idfhgihiudfh iugfh ihgdiuh ui hidf hiu dfhi hdfigh ifh idfh gih hih giudrh idfg </p>
                    </div>
                </div>
            </div>
            <div className="d-flex align-center justify-center flex-column">
                <h1 className="mb-1-r">Contactez nous</h1>
                <p>Vous pouvez nous contacter <a href="mailto:exemplemail@mail.fr">ici</a>.</p>
            </div>
        </div>
    )
}

export default About