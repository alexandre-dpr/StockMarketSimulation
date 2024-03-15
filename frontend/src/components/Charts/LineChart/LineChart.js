import React, { useRef } from 'react';
import { Line } from "react-chartjs-2";
import "./LineChart.scss";

const LineChart = ({ data, labels, intervalLabelsCount,style }) => {
    const chartRef = useRef(null);
    const skipInterval = Math.ceil(labels.length / intervalLabelsCount);
    const chartData = {
        labels: labels,
        datasets: [
            {
                data: data,
                borderColor: "black",
                spanGaps: true,
                pointRadius: 0,
                pointHitRadius: 10,
            },
        ],
    };

    const options = {
        maintainAspectRatio: false,
        plugins: {
            legend: {
                display: false,
            },
            tooltip: {
                enabled: true,
                mode: 'index',
                intersect: false,
            },
        },
        scales: {
            x: {
                grid: {
                    display: false,
                },
                ticks: {
                    callback: function (val, index) {
                        return index % skipInterval === 0 ? this.getLabelForValue(val) : '';
                    },
                    maxRotation: 0,
                    autoSkip: false,
                },
            },
            y: {
                grid: {
                    display: false,
                }
            },
        },
        interaction: {
            mode: 'nearest',
            axis: 'x',
            intersect: false,
        },
        animation: {
            duration: 0,
        },
    };

    return (
        <div>
            <Line style={style} ref={chartRef} data={chartData} options={options} />
        </div>
    );
};

export default LineChart;
