import React, { useRef } from 'react';
import { Line } from "react-chartjs-2";
import "./LineChart.scss";

const LineChart = ({ data, labels, intervalLabelsCount,style, lineColor }) => {
    const chartRef = useRef(null);
    const skipInterval = Math.ceil(labels.length / intervalLabelsCount);
    const chartData = {
        labels: labels,
        datasets: [
            {
                data: data,
                borderColor: lineColor,
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
                position: 'right',
                grid: {
                    display: true,
                    color: "rgba(0, 0, 0, 0.1)",
                },
                ticks: {

                }
            }
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
