import React from 'react';
import {Pie} from "react-chartjs-2";

const Camembert = ({data}) => {
    const data2 = {
        labels: data.label,
        datasets: [
            {
                label: '%',
                data: data.data,
                hoverOffset: 4,
            },
        ],
    };
    const options = {
        maintainAspectRatio: false,
        plugins: {
            legend: {
                display: false,
            }
        },
    }
    return (
        <Pie  data={data2} options={options} />
    );
};

export default Camembert;