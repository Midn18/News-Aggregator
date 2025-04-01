package com.example.newsAggregator.repository

import com.example.newsAggregator.model.Source
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface SourceRepository : MongoRepository<Source, String>