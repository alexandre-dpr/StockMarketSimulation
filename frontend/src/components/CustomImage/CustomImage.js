import React, { useEffect, useRef } from 'react';


function CustomImage({ src, alt, style }) {
    const canvasRef = useRef(null);

    useEffect(() => {
        const image = new Image();
        image.crossOrigin = "Anonymous"; // Nécessaire pour charger des images d'autres domaines
        image.src = src;
        image.onload = () => {
                const canvas = canvasRef.current;
            if(canvas !== null){
                const ctx = canvas.getContext('2d');
                canvas.width = image.width;
                canvas.height = image.height;
                ctx.drawImage(image, 0, 0);

                const imageData = ctx.getImageData(0, 0, canvas.width, canvas.height);
                const data = imageData.data;

                for (let i = 0; i < data.length; i += 4) {
                    // Change les pixels blancs en noirs
                    if (data[i] === 255 && data[i + 1] === 255 && data[i + 2] === 255) {
                        data[i] = 0;
                        data[i + 1] = 0;
                        data[i + 2] = 0;
                    }
                }

                ctx.putImageData(imageData, 0, 0);
            }

        };
    }, [src]);

    return <canvas ref={canvasRef} style={style} alt={alt} />;
}

export default CustomImage;