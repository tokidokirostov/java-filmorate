package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
//import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;
import java.util.Optional;

@Service
public class MpaService {
    MpaStorage mpaStorage;
   //MpaDao mpaDao;

    /*public MpaService(MpaDao mpaDao) {
        this.mpaDao = mpaDao;
    }*/

    public MpaService(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    /*public Optional<Mpa> findById(Long id) {
            return mpaDao.findById(id);
        }*/
    public Optional<Mpa> findById(Long id) {
        return mpaStorage.findMpaById(id);
    }

    public List<Optional<Mpa>> findAllMpa() {
        return mpaStorage.findAllMpa();
    }
}
