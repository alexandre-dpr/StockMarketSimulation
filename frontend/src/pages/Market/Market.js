import React from 'react'
import './Market.scss'
import { useTranslation } from 'react-i18next';
import { CompactTable } from '@table-library/react-table-library/compact';

const nodes = [
    {
        id: '0',
        name: 'Shopping List',
        deadline: new Date(2020, 1, 15),
        type: 'TASK',
        isComplete: true,
        nodes: 3,
    },
];

const COLUMNS = [
    { label: 'Task', renderCell: (item) => item.name },
    {
        label: 'Deadline',
        renderCell: (item) =>
            item.deadline.toLocaleDateString('en-US', {
                year: 'numeric',
                month: '2-digit',
                day: '2-digit',
            }),
    },
    { label: 'Type', renderCell: (item) => item.type },
    {
        label: 'Complete',
        renderCell: (item) => item.isComplete.toString(),
    },
    { label: 'Tasks', renderCell: (item) => item.nodes },
];

function Market() {
    const data = { nodes };
    const { t } = useTranslation();
    return (
        <div className='containerPage'>
            <h1>
                {t('market.market')}
            </h1>
            <CompactTable columns={COLUMNS} data={data} />

        </div>
    )
}

export default Market