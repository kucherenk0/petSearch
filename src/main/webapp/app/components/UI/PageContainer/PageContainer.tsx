import React, { FC } from 'react';
import './PageContainer.styles.scss';

const PageContainer: FC = props => {
	return <div className={'petSearchPageContainer'}>{props.children}</div>;
};

export default PageContainer;
