import { Kafka } from 'kafkajs';

// Kafka 클러스터 설정
const kafka = new Kafka({
    clientId: 'console-consumer',
    brokers: ['localhost:9092'],
});

// 프로듀서 설정
const kafkaProducer = kafka.producer();

// 컨슈머 설정
const kafkaConsumer = kafka.consumer({ groupId: 'console-consumer-67957' });

// Kafka 설정 및 시작 함수
const startKafka = async () => {
    try {
        // 프로듀서 연결
        await kafkaProducer.connect();
        console.log('Kafka producer connected');

        // 컨슈머 연결
        await kafkaConsumer.connect();
        console.log('Kafka consumer connected');

        // // 구독 설정
        // await kafkaConsumer.subscribe({ topic: 'expiry-topic', fromBeginning: true });
        // console.log('Kafka consumer subscribed to topic');
        //
        // // 메시지 처리 함수 실행
        // await kafkaConsumer.run({
        //     eachMessage: async ({ topic, partition, message }) => {
        //         console.log(`Received message: ${message.value.toString()}`);
        //         // 메시지를 처리하는 로직을 추가하세요
        //     },
        // });
        //
        // // 컨슈머가 계속 실행될 수 있도록 설정
        // console.log('Kafka consumer is running and processing messages');

    } catch (error) {
        console.error('Error in Kafka setup:', error);
        process.exit(1); // 오류 발생 시 프로세스 종료
    }
};

// Kafka 프로듀서와 컨슈머를 시작
startKafka();

// 프로듀서와 컨슈머를 내보냅니다
export { kafkaProducer, kafkaConsumer };
