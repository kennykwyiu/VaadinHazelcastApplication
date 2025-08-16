package com.example.config;


import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastConfig {

    @Value("${hazelcast.cluster.name}")
    private String clusterName;

    @Value("${hazelcast.network.port}")
    private int port;

    @Value("${hazelcast.network.port-auto-increment}")
    private boolean portAutoIncrement;

    @Value("${hazelcast.network.port-count}")
    private int portCount;

    @Value("${hazelcast.multicast.enabled}")
    private boolean multicastEnabled;

    @Value("${hazelcast.multicast.group}")
    private String multicastGroup;

    @Value("${hazelcast.multicast.port}")
    private int multicastPort;

    @Bean
    public HazelcastInstance hazelcastInstance() {
        Config config = new Config();
        config.setClusterName(clusterName);

        NetworkConfig networkConfig = config.getNetworkConfig();
        networkConfig.setPort(port)
                .setPortAutoIncrement(portAutoIncrement)
                .setPortCount(portCount);

        JoinConfig joinConfig = networkConfig.getJoin();
        joinConfig.getTcpIpConfig().setEnabled(false);
        joinConfig.getMulticastConfig()
                .setEnabled(multicastEnabled)
                .setMulticastGroup(multicastGroup)
                .setMulticastPort(multicastPort);

        return Hazelcast.newHazelcastInstance(config);
    }
}
