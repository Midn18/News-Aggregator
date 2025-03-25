package com.example.newsAggregator.repository

import com.example.newsAggregator.entity.SourceDB
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface SourceRepository : MongoRepository<SourceDB, String>