import React, { FC } from 'react';
import Carousel from 'antd/es/carousel';
import './DogCarousel.scss';

type TSlide = {
	bigTitle?: string;
	subtitle?: string;
	title?: string;
	image: string;
	article?: string;
};

const DogCarousel: FC = props => {
	const SLIDES = [
		{
			bigTitle: 'PetSearch',
			subtitle: 'Если у вас потерялась собака - не переживайте, мы её найдём!',
			image: 'content/images/dog1.png',
		},
		{
			title: 'Современные технологии',
			subtitle: `Наш сервис использует данные с городских камер видеонаблюдения,\n чтобы найти вашего питомца.
		\n Мы используем технологии ИИ для обработки снимков с камер видеонаблюдения.`,
			image: 'content/images/dog2.png',
		},
		{
			title: 'Скорость и точность',
			subtitle: `Наша система может быстро и точно находить собак с точностью 90% на снимках\n Определять наличие хозяина с точностью до 95% \nи определять породу, цвет и длину хвоста с точностью до 80%`,
			image: 'content/images/dog3.png',
		},
	];

	const renderSlide = (slide: TSlide) => {
		return (
			<div>
				<div className={'dogSlideItem'}>
					{window.innerWidth > 1400 && <img src={slide.image} />}
					<div className={'dogSlideText'}>
						{slide.bigTitle ? (
							<h1>{slide.bigTitle}</h1>
						) : (
							<h2>{slide.title}</h2>
						)}
						{slide.subtitle && (
							<p className={'carouselSubtitle'}>{slide.subtitle}</p>
						)}
					</div>
				</div>
			</div>
		);
	};

	return (
		<Carousel draggable autoplaySpeed={5000} speed={800} className={'dogCarousel'}>
			{SLIDES.map(item => renderSlide(item))}
		</Carousel>
	);
};

export default DogCarousel;
